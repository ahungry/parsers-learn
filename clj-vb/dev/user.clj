;; (setq cider-clojure-cli-aliases ":env/dev")
(ns user
  (:require
   [clojure.main]
   [clj-vb.core :as vb]))

;; Load all the stuff like doc etc.
(defn go []
  (apply require clojure.main/repl-requires))

(go)
