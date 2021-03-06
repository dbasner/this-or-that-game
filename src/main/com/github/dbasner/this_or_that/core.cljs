(ns com.github.dbasner.this-or-that.core
  (:require
    [goog.functions :as gfunctions]
    [oops.core :refer [ocall]]
    [com.github.dbasner.this-or-that.game-logic]
    [com.github.dbasner.this-or-that.interaction :refer [trigger-render! init-watchers!]]))

(def init!
  (gfunctions/once
    (fn []
      (js/console.log "Initializing This-Or-That!!!!")
      (init-watchers!)
      (trigger-render!))))

(defn on-refresh
  []
  (trigger-render!))


(ocall js/window "addEventListener" "load" init!)