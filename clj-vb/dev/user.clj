;; (setq cider-clojure-cli-aliases ":env/dev")
(ns user
  (:require [clj-vb.core :as vb]
            [clojure.repl :refer :all]))

(prn "Loaded my user.clj")

(defn blub [] 3)
