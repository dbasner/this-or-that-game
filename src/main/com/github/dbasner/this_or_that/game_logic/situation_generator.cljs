(ns com.github.dbasner.this-or-that.game-logic.situation-generator
  "generate the random Situation sentences")

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

;; -----------------------------------------------------------------------------
;; Public API

(defn random-situation
  []
  {:verb (rand-verb)
   :count-modifier (rand-count-modifier)
   :count (rand-situation-count 20)
   :noun (rand-noun)})
