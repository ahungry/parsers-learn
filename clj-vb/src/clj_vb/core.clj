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

(defn ast-start-rule [& xs] xs)
(defn ast-single-rule [& xs] xs)
(defn ast-atom [& xs] xs)
(defn ast-logical-expr [& xs] xs)
(defn ast-statement [& xs] xs)
(defn ast-if-then [& xs] xs)
(defn ast-prop-acc [& xs] xs)
(defn ast-comparison-expr [& xs] xs)
(defn ast-mcall [& xs] xs)
(defn ast-class-def [& xs] xs)

(defn ast-method [visibility sub name arglist & xs]
  (prn (str "New method: " name)))


(defn process-tree [tree]
  (clojure.walk/prewalk-replace
   {:startRule 'ast-start-rule
    :singleRule 'ast-single-rule
    :logical_expr 'ast-logical-expr
    :statement 'ast-statement
    :method 'ast-method
    :class_def 'ast-class-def
    :ifthen 'ast-if-then
    :prop_acc 'ast-prop-acc
    :mcall 'ast-mcall
    :comparison_expr 'ast-comparison-expr
    :atom 'ast-atom}
   tree))
