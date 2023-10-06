(ns app.view-types.table
  (:require
   contrib.str
   [hyperfiddle.electric :as e]
   [app.view-types.view-options :as view-options]
   [hyperfiddle.electric-dom2 :as dom]
   [hyperfiddle.electric-ui4 :as ui]
   [app.dom-utils :as dom-utils]))


;; Works on lists of maps, or a map itself.
(e/defn DatifyTableServer
  [{::view-options/keys [server-value id]}]
  (let [table-type :map-only
        data       (map (fn [[k v]]
                          [{::view-options/id           (str (random-uuid))
                            ::view-options/server-value k}
                           {::view-options/id           (str (random-uuid))
                            ::view-options/server-value v}])
                     server-value)]
    (case table-type
      :map-only
      {::view-options/render-options {::view-options/id id
                                      ::rows            (map (fn [row] (map #(select-keys % [::view-options/id]) row)) data)}
       ::view-options/further-render (apply concat data)})))

(e/defn DatifyTableClient
  [{::view-options/keys [id]
    ::keys              [rows] :as input}]
  (dom-utils/with-mounted-edge id
    (dom/table
      (dom/thead
        (dom/tr
          (dom/th (dom/text "Key"))
          (dom/th (dom/text "Value"))))
      (dom/tbody
        (e/for [row rows]
          (dom/tr
            (e/for [col row]
              (dom/td
                (dom/props {:id (::view-options/id col)})
                ))))))))