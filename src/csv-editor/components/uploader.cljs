(ns csv-editor.components.uploader
  (:require [rum.core :as rum]
            [goog.labs.format.csv :as csv]))

(defn valid-file? [file]
  ;For some reason file type can be changed if file opened in MacOS
   (and (or 
          (= file.type "text/csv") 
          (= file.type "application/vnd.ms-excel")) 
      (< file.size 100)))

(def parse-csv #(-> % .-target .-result csv/parse))

(defn concat-with-numbers [table]
  (into [] (concat [(first table)] (map #(-> [(first %) (int (second %))]) (rest table)))))
                             
(defn valid-table? [table]
  (every? #(= (count %) 2) table))

(defn build-reader [on-success on-error]
  (let [reader (js/FileReader.)
        handle-loadend #(if (valid-table? %) (on-success %) (on-error))]
    (set! (.-onloadend reader) #(-> % parse-csv js->clj concat-with-numbers handle-loadend))
    reader))

(rum/defc Uploader [{:keys [handle-file handle-error handle-reset error? rows]}]
  (let [reader (build-reader handle-file handle-error)
        run-reader #(if (valid-file? %) (.readAsText reader %) (handle-error))
        on-change (fn [ev]
                    (let [file (-> ev .-target .-files (aget 0))]
                       (when-not (undefined? file) (run-reader file))))]
      [:div.uploader
        (when (empty? rows)
          [:label.upload-button
            [:input {:type "file" :accept ".csv" :on-change on-change}]])
        (when (or (false? (empty? rows)) (true? error?))
          [:button {:on-click handle-reset} "Reset"])
        (when error?
          [:div.upload-error
            [:span "Validation error"]])]))
