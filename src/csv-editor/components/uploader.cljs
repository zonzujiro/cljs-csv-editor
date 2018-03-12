(ns csv-editor.components.uploader
  (:require [rum.core :as rum]
            [goog.labs.format.csv :as csv]))

(defn valid-file? [file]
   (js/console.log file)
   (and (or (= file.type "text/csv") (= file.type "application/vnd.ms-excel")) (< file.size 100)))

(def get-file #(-> % .-target .-files (aget 0)))
(def parse-csv #(-> % .-target .-result csv/parse))

(defn convert-to-numbers [table]
  (into [] (concat [(first table)] (map #(-> [(first %) (int (second %))]) (rest table)))))
                             
(defn valid-table? [table]
  (every? #(= (count %) 2) table))

(defn build-reader [on-success on-error]
  (let [reader (js/FileReader.)
        handle-loadend #(if (valid-table? %) (on-success %) (on-error))]
    (set! (.-onloadend reader) #(-> % parse-csv js->clj convert-to-numbers handle-loadend))
    reader))

(rum/defc Uploader [{:keys [handle-file handle-error handle-reset error? rows]}]
  (let [reader (build-reader handle-file handle-error)
        run-reader #(if (valid-file? %) (.readAsText reader %) (handle-error))
        on-change #(when-not (undefined? (get-file %)) (run-reader (get-file %)))]
      [:div.uploader
        (when (empty? rows)
          [:label.upload-button
            [:input {:type "file" :accept ".csv" :on-change on-change}]])
        (when (or (false? (empty? rows)) (true? error?))
          [:button {:on-click handle-reset} "Reset"])
        (when error?
          [:div.upload-error
            [:span "Validation error"]])]))
