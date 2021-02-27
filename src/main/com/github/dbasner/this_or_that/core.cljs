(ns com.github.dbasner.this-or-that.core
  (:require
    [goog.functions :as gfunctions]
    [oops.core :refer [ocall]]))

(def init!
  (gfunctions/once
    (fn []
      (js/console.log "Initializing This-Or-That!!!!"))))

(ocall js/window "addEventListener" "load" init!)