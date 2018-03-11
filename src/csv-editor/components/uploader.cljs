(ns csv-editor.components.uploader
  (:require [rum.core :as rum]
            [goog.labs.format.csv :as csv]))

(defn validate [file]
   (< file.size 100))

(defn to-vector [arrays]
  (map vector arrays))

(defn build-reader [on-loadend]
  (let [reader (js/FileReader.)]
    (set! (.-onloadend reader) #(-> % .-target .-result csv/parse js->clj on-loadend))
    reader))

(rum/defc Uploader [{:keys [handle-file handle-error handle-reset error? rows]}]
  (let [reader (build-reader handle-file)
        run-reader #(if (validate %) (.readAsText reader %) (handle-error))
        get-file #(-> % .-target .-files (aget 0))
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
