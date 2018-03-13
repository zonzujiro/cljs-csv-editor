(ns csv-editor.components.table
  (:require [rum.core :as rum]))

(rum/defc Cell [{:keys [value on-change number?]}]
  (let [handle-change #(if number? (on-change (int %)) (on-change %))]
    [:td
      [:input {:value value 
               :on-change #(-> % .-target .-value handle-change)
               :type (if number? "number" "text")}]]))

(rum/defc Row [{:keys [row-index row-items on-change]}]
  [:tr {:key row-index}
    (map-indexed 
      (fn [cell-index value]
        (let [props {:on-change #(on-change (assoc row-items cell-index %))
                     :number? (integer? value)
                     :value value}]
          (rum/with-key (Cell props) cell-index)))
      row-items)])

(rum/defc Table [{:keys [rows on-change]}]
  [:table
    [:tbody
      (map-indexed 
        (fn [index row]
          (let [props {:row-index index
                       :row-items row
                       :on-change #(on-change (assoc rows index %))}]
            (rum/with-key (Row props) index)))
       rows)]])
