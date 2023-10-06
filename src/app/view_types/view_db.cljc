(ns app.view-types.view-db)

(defonce !id->view-fn 
  #?(:cljs (atom {})
     :clj nil))
