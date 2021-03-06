(ns com.github.dbasner.this-or-that.interaction
  (:require [oops.core :refer [oset!]]
            [goog.dom :as gdom]
            [goog.functions :as gfunctions]
            [com.github.dbasner.this-or-that.state :refer [state]]
            [hiccups.runtime :as hiccups]
            [com.github.dbasner.this-or-that.html :refer [ThisOrThatGame]]
            [com.github.dbasner.this-or-that.game-logic :as game-logic])
  (:require-macros [hiccups.core :as hiccups]))

(defn set-app-html!
  [html-str]
  (let [el (gdom/getElement "appContainer")]
    (oset! el "innerHTML" html-str)))

(defn trigger-render!
  []
  (swap! state identity))

(defn render-ui!
  [_ _kwd _prev-state new-state]
  (set-app-html! (hiccups/html (ThisOrThatGame new-state))))


(def init-watchers!
  (gfunctions/once
    (fn []
      (add-watch state :render-ui render-ui!))))

