(ns com.github.dbasner.this-or-that.game-logic
  (:require [ajax.core :refer [GET POST]]))

;;OK so how does this game work?
;; what is it's core?
;; generate two possibilities
;; a or b
;; would you rather
;; [verb] [count] [nouns] or [verb] [count] [nouns] ?

;; start by doing just generating a basic string

(def verbs ["eat" "cook" "punch" "get punched by"
            "get kicked by" "kick" "date" "kiss"
            "marry" "love" "get laughed at by"
            "live with" "barbecue" "get yelled at by"
            "upset" "bother" "take a roadtrip with"
            "fly sitting next to" "fall for"
            "get trapped on a desert island with"
            "become conjoined twins with"
            "be trapped with" "be looked up to by"
            "settle down with" "burn" "get wasted with"])

(def count-modifiers ["roughly" "about" "precisely" "exactly" "almost" "just about"])

(def nouns ["sharks" "people" "dogs" "turtles"
            "chairs" "tables" "dinosaurs" "computers"
            "men" "women" "cats" "rats" "couches"
            "airplanes" "cars" "haters" "phones"
            "jugglers" "clowns" "doctors" "therapists"
            "lawyers" "artists" "hipsters"])

(defn rand-verb [] (rand-nth verbs))
(defn rand-count-modifier [] (rand-nth count-modifiers))
(defn rand-situation-count [n] (+ (rand-int n) 2))
(defn rand-noun [] (rand-nth nouns))

(defn generate-situation []
  {:verb (rand-verb)
   :count-modifier (rand-count-modifier)
   :count (rand-situation-count 20)
   :noun (rand-noun)})

(defn handler [response]
  (.log js/console (str response)))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "something bad happened: " status " " status-text)))


(comment
  (GET "https://wordsapiv1.p.rapidapi.com/words/?partOfSpeech=noun?random=true"
       {:headers {:x-rapidapi-key  "YOUR_API_KEY_HERE"
                  :x-rapidapi-host "wordsapiv1.p.rapidapi.com"}}
       :handler handler
       :error-handler error-handler)

  (GET "https://webknox-words.p.rapidapi.com/words/{WORD}/plural"
       {:headers {:x-rapidapi-key  "YOUR_API_KEY_HERE"
                  :x-rapidapi-host "wordsapiv1.p.rapidapi.com"}}
       :handler handler
       :error-handler error-handler))









