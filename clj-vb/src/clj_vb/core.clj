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


(defn cleaner [xs]
  (filter #(not (string? %)) xs))

(defn ast-start-rule [& xs] xs)
(defn ast-single-rule [& xs] xs)

(defn ast-atom [x]
  (if (string? x) (symbol x) x))

(defn ast-value [& xs] xs)
(defn ast-string [x]
  (if (string? x) (read-string x) x))

(defn ast-logical-expr [& xs] xs)
(defn ast-logical-entity [x] x)
(defn ast-statement [x & _] (list x))
(defn ast-comp-operator [x] (symbol x))

(defn ast-condition [[cond1 pred cond2] & xs]
  `(~(symbol pred) ~@cond1 ~@cond2))

(defn ast-if-then [& xs] xs)

(defn ast-comparison-expr [le1 op le2]
  `(~op ~le1 ~le2))

(defn ast-mcall [& xs]
  (let [[obj prop & args] (cleaner xs)]
    `(my-call ~obj ~prop ~@args)))

(defn ast-prop-acc [& xs]
  (let [clean (cleaner xs)]
    `(my-prop ~@clean)))

(defn ast-class-def [& xs]
  (let [clean (cleaner xs)]
    `(my-defclass ~@clean)))

(defn ast-arglist [open & xs]
  (take (- (count xs) 1) xs))

(defn ast-method [& xs]
  (let [clean (cleaner xs)
        [name arglist & statements] clean]
    `(my-defmethod ~name ~arglist ~@statements)))

(defn process-tree [tree]
  (clojure.walk/prewalk-replace
   {:startRule 'ast-start-rule
    :singleRule 'ast-single-rule
    :logical_expr 'ast-logical-expr
    :logical_entity 'ast-logical-entity
    :condition 'ast-condition
    :statement 'ast-statement
    :method 'ast-method
    :string 'ast-string
    :value 'ast-value
    :class_def 'ast-class-def
    :ifthen 'ast-if-then
    :prop_acc 'ast-prop-acc
    :mcall 'ast-mcall
    :comp_operator 'ast-comp-operator
    :comparison_expr 'ast-comparison-expr
    :arglist 'ast-arglist
    :atom 'ast-atom}
   tree))
