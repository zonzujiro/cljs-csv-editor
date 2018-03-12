(ns csv-editor.core
  (:require [rum.core :as rum]
            [csv-editor.components.table :refer [Table]]
            [csv-editor.components.uploader :refer [Uploader]]
            [csv-editor.components.widget :refer [Widget]]))
  
(def init-state {:rows [] :error? false})

(rum/defcs App < (rum/local init-state ::state)
  [{state ::state}]
  (js/console.log (clj->js @state))
  [:div.root-container
   [:h1 "CSV table editor"]
   (Uploader {:handle-file #(swap! state assoc :rows % :error? false)
              :handle-error #(swap! state assoc :error? true)
              :handle-reset #(reset! state init-state)
              :error? (:error? @state) 
              :rows (:rows @state)})
   (when (and (false? (:error? @state)) (false? (empty? (:rows @state))))
     [:div.content
      (Table {:rows (:rows @state)
              :on-change #(swap! state assoc :rows %)})
      (Widget {:rows (rest (:rows @state))})])])

(rum/mount (App)
           (. js/document (getElementById "app")))

