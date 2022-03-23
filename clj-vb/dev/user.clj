;; (setq cider-clojure-cli-aliases ":env/dev")
(ns user
  (:require [clj-vb.core :as vb]))

;; Load all the stuff like doc etc.
(apply require clojure.main/repl-requires)

(defn blub [] 3)
