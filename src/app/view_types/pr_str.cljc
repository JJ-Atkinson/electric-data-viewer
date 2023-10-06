(ns app.view-types.pr-str
  (:require
   contrib.str
   [hyperfiddle.electric :as e]
   [app.view-types.view-options :as view-options]
   [hyperfiddle.electric-dom2 :as dom]
   [hyperfiddle.electric-ui4 :as ui]
   [app.dom-utils :as dom-utils]))


;; Works on lists of maps, or a map itself.
(e/defn DatifyPrStrServer
  [{::view-options/keys [server-value id]}]
  {::view-options/render-options {::data            (pr-str server-value)
                                  ::view-options/id id}
   ::view-options/further-render []})

(e/defn DatifyPrStrClient
  [{::view-options/keys [id]
    ::keys              [data] :as e}]
  (dom/div
    (dom/text data)))
