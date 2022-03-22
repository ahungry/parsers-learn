(ns clj-vb.core
  (:require [clj-antlr.core :as antlr]
            ;; [clojure.core :refer [pprint]]
            ))

;; (clojure.pprint 3)

(defn -main [& xs]
  (def dumb (antlr/parser (str (System/getProperty "user.dir") "/DUMB.g4")))
  (clojure.pprint/pprint (dumb (slurp "DUMB.asp"))))

(defn main [& xs]
  (apply -main xs))
