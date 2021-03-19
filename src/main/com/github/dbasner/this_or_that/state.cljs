(ns com.github.dbasner.this-or-that.state)

(def initial-state
  {:player-ids []
   :current-voter-index 0
   :scores {}
   :current-round-votes {:situationA []
                         :situationB []}
   :situationA nil
   :situationB nil
   :rounds-left 5
   :winners []})

(def state (atom initial-state))
