(ns clj-vb.core
  (:require [clj-antlr.core :as antlr]
            ;; [clojure.core :refer [pprint]]
            ))

;; (clojure.pprint 3)

(defn tree []
  (def dumb (antlr/parser (str (System/getProperty "user.dir") "/DUMB.g4")))
  (dumb (slurp "DUMB.asp")))

(defn -main [& xs]
  (-> (tree) clojure.pprint/pprint))

(defn main [& xs]
  (apply -main xs))
