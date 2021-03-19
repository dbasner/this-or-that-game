(ns com.github.dbasner.this-or-that.game-logic
  "core game logic"
  (:require
    [ajax.core :refer [GET POST]]
    [com.github.dbasner.this-or-that.game-logic.situation-generator :refer [random-situation]]
    [com.github.dbasner.this-or-that.state :refer [state initial-state]]))

(defn add-vote
  [state player-id situation-id]
  (update-in state [:current-round-votes situation-id] conj player-id))

(defn rotate-to-next-player
  "increment the current player index or reset to 0 if we are ready for the next round"
  [state]
  (let [num-players (-> state :player-ids count)
        max-player-idx (dec num-players)
        voter-index (:current-voter-index state)]
    (if (< voter-index max-player-idx)
      (update-in state [:current-voter-index] inc)
      (assoc state :current-voter-index 0))))

(defn increment-score [state player-id]
  (update-in state [:scores player-id] inc))

(defn increment-player-scores
  "for a collection of player ids, increment the score"
  [state player-ids]
  (reduce #(increment-score %1 %2) state player-ids))

(defn add-scores
  [old-state]
  (let [situationA-votes  (get-in old-state [:current-round-votes :situationA])
        ;; FYI - these are all the same
        ; situationB-votes (:situationB (:current-round-votes old-state))
        ; situationB-votes (-> old-state :current-round-votes :situationB)
        situationB-votes (get-in old-state [:current-round-votes :situationB])

        situationA-count (count situationA-votes)
        situationB-count (count situationB-votes)]
    (cond
      (= situationA-count situationB-count) (increment-player-scores old-state (:player-ids state))
      (> situationA-count situationB-count) (increment-player-scores old-state situationA-votes)
      (< situationA-count situationB-count) (increment-player-scores old-state situationB-votes)
      :else old-state)))

(defn generate-new-situations
  [state]
  (-> state
      (assoc :situationA (random-situation))
      (assoc :situationB (random-situation))))

(defn reset-votes
  [state]
  (-> state
      (assoc-in [:current-round-votes :situationA] [])
      (assoc-in [:current-round-votes :situationB] [])))

  ;; FYI - another way to write this
  ; (update-in state [:current-round-votes] merge {:situationA []
  ;                                                :situationB []}))

(defn dec-rounds-left
  [state]
  (update-in state [:rounds-left] dec))

(defn decide-winners
  [state]
  (let [scores (:scores state)
        max-value (apply max (vals scores))
        winners (filter #(= (second %) max-value) scores)]
    (assoc-in state [:winners] (keys winners))))

(defn prepare-next-round [state]
  (-> state
      add-scores
      dec-rounds-left
      reset-votes
      generate-new-situations))

(defn apply-votes-and-rotate-player [state player-id situation-id]
  (-> state
      (add-vote player-id situation-id)
      rotate-to-next-player))

(defn process-vote
  "processes voting"
  [state player-id situation-id]
  (let [default-next-state (apply-votes-and-rotate-player state player-id situation-id)
        is-new-round? (zero? (:current-voter-index default-next-state))
        new-round-state (prepare-next-round default-next-state)
        last-round? (zero? (:rounds-left new-round-state))
        game-over? (and is-new-round? last-round?)
        start-next-round? (and is-new-round? (not last-round?))]
    (cond
      game-over?        (decide-winners new-round-state)
      start-next-round? new-round-state
      ;; else switch to the next player
      :else             default-next-state)))

(defn generate-player-ids
  "returns a Vector of player id Strings: ['Player-1', 'Player-2', 'Player-3']"
  [player-count]
  (mapv #(str "Player-" (inc %)) (range player-count)))
  ;; Original:
  ; (vec (for [n (range player-count)]
  ;        (str "Player-" (+ n 1)))))

(defn generate-new-game-scores [player-ids]
  (apply assoc {} (interleave player-ids
                              (repeat (count player-ids) 0))))

(defn reset-game-state
  "reset the game to a fresh state"
  [state player-count]
  (let [player-ids (generate-player-ids player-count)]
    (-> state
        (assoc-in [:player-ids] player-ids)
        (assoc-in [:scores] (generate-new-game-scores player-ids))
        (assoc-in [:current-voter-index] 0)
        (generate-new-situations))))


; (defn handler [response]
;   (.log js/console (str response)))
;
; (defn error-handler [{:keys [status status-text]}]
;   (.log js/console (str "something bad happened: " status " " status-text)))
;
;
; (comment
;   (GET "https://wordsapiv1.p.rapidapi.com/words/?partOfSpeech=noun?random=true"
;        {:headers {:x-rapidapi-key  "YOUR_API_KEY_HERE"
;                   :x-rapidapi-host "wordsapiv1.p.rapidapi.com"}}
;        :handler handler
;        :error-handler error-handler)
;
;   (GET "https://webknox-words.p.rapidapi.com/words/{WORD}/plural"
;        {:headers {:x-rapidapi-key  "YOUR_API_KEY_HERE"
;                   :x-rapidapi-host "wordsapiv1.p.rapidapi.com"}}
;        :handler handler
;        :error-handler error-handler))
