(ns csv-editor.components.widget
  (:require [rum.core :as rum]))

(rum/defc Widget [{:keys [rows]}]
  (when (not-empty rows) 
    (let [sum (reduce + (map #(-> % second int) rows))]
      [:div.widget
        [:h2 "Aggregated results"
          [:section.sum
            [:span "Sum"]
            [:p sum]]
          [:section.average
            [:span "Average"]
            [:p (int (/ sum (count rows)))]]]])))
    
