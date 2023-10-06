(ns app.dom-utils
  (:require
   [hyperfiddle.electric :as e]
   [hyperfiddle.electric-dom2 :as dom])
  #?(:cljs (:require-macros [app.dom-utils :refer [with-mounted-edge]])))

#?(:cljs (defonce !id->!tf (atom {})))

#?(:cljs (defn id->!tf 
           [id]
           (if (contains? @!id->!tf id)
             (get @!id->!tf id)
             (let [!tf (atom (dom/by-id id))]
               (swap! !id->!tf assoc id !tf)
               !tf))))

#?(:cljs (defonce mount-watcher
           (let [callback (fn [^js mutationList ^js observer]
                            (doseq [^js mutationElement mutationList]
                              (let [mutation-type (.-type mutationElement)
                                    attribute-name (.-attributeName mutationElement)]
                                (when (and (= mutation-type "attributes")
                                        (= attribute-name "id"))
                                  (when-let [old-id (.-oldValue mutationElement)]
                                    (reset! (id->!tf old-id) nil))
                                  
                                  (reset! (id->!tf (.-id (.-target mutationElement))) (.-target mutationElement)))
                                (when (= mutation-type "childList")
                                  (doseq [node (.-removedNodes mutationElement)]
                                    (when-let [old-id (.-id node)]
                                      (reset! (id->!tf old-id) node)))))
                              ))

                 observer (new js/MutationObserver callback)
                 config   #js {:childList true 
                               :subtree true
                               :attributeFilter #js ["id"]
                               :attributeOldValue true}]
             (when-not (.-appDomUtilsWatcherMounted js/document)
               (.observe observer (.-body js/document) config))
             (set! (.-appDomUtilsWatcherMounted js/document) true))))


(defmacro with-mounted-edge
  [id & body]
  `(let [element# (e/watch (id->!tf ~id))
         mounted?# (boolean element#)]
     (js/console.log "with-mounted-edge id" ~id "mounted?" mounted?# "element" element#)
     (when element#
       (dom/with element#
         ~@body))))
