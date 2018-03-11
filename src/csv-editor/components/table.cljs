(ns csv-editor.components.table
  (:require [rum.core :as rum]))

(rum/defc Cell [{:keys [value on-change]}]
  [:td
    [:input {:value value :on-change #(-> % .-target .-value on-change)}]])

(rum/defc Row [{:keys [row-index row-items on-change]}]
  [:tr {:key row-index}
    (map-indexed 
      (fn [cell-index row]
        (Cell {:on-change #(on-change (assoc row-items cell-index %))
               :value row}))
      row-items)])

(rum/defc Table [{:keys [rows on-change]}]
  [:table
    [:tbody
      (map-indexed 
        (fn [index row]
          (Row {:row-index index
                :row-items row
                :on-change #(on-change (assoc rows index %))})) 
       rows)]])
