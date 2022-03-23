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


(def *bindings (atom {}))

(defn my-defvar [vars]
  (map (fn [v]
         (swap! *bindings (fn [m] (update-in m [(keyword v)] (fn [_] :nil)))))
       vars))

(defn my-set [sym val]
  (swap! *bindings (fn [m] (update-in m [(keyword sym)] (fn [_] val)))))

(defn recursive-filter
  "Apply a filter function F to each element in each
  collection (recursively) in XS."
  [f xs]
  (map (fn [x]
         (if (coll? x)
           (recursive-filter f (filter f x))
           x)) (filter (fn [x] (or (coll? x) (f x))) xs)))

(defn is-skippable? [x]
  (or (= "\r\n" x)
      (= "<EOF>" x)
      (= nil x)
      (and (coll? x) (= :comment (first x)))))

(def is-keepable? (complement is-skippable?))

(defn get-tree []
  (->> (tree)
       (recursive-filter is-keepable?)))

(defn cleaner [xs]
  (filter #(not (string? %)) xs))

(defn newline-cleaner [xs]
  (filter #(not (= "\r\n" %)) xs))

(defn ast-start-rule [& xs] `(do ~@xs))
(defn ast-single-rule [& xs] (first xs))

(defn ast-atom [x]
  (if (string? x) (symbol (clojure.string/lower-case x)) x))

(defn ast-value [& xs] xs)
(defn ast-string [x]
  (if (string? x) (read-string x) x))

(defn ast-comment [comment-string]
  `(my-comment ~comment-string))

(defn ast-logical-expr [le1 & xs]
  (if (not (= 2 (count xs)))
    le1
    (let [[op le2] xs]
      `(~(symbol op) ~le1 ~le2)
      )))

(defn ast-logical-entity [x] x)

(defn ast-statement [& xs]
  xs)

(defn ast-comp-operator [x] (symbol x))

(defn ast-condition [x]
  x)

(defn ast-if-then [& xs]
  (let [[pred & statements] (cleaner xs)]
    `(if ~pred ~@statements)))

(defn ast-comparison-expr [le1 op le2]
  `(~op ~le1 ~le2))

(defn ast-mcall [& xs]
  (let [[obj prop & args] (cleaner xs)]
    `(my-call ~obj ~prop (quote ~@args))))

(defn ast-prop-acc [& xs]
  (let [clean (cleaner xs)]
    `(my-prop ~@clean)))

(defn ast-class-def [& xs]
  (let [clean (cleaner xs)]
    `(my-defclass ~@clean)))

(defn ast-arglist [open & xs]
  (take (- (count xs) 1) xs))

(defn ast-args [x] x)

(defn ast-method [& xs]
  (let [clean (cleaner xs)
        [name arglist & statements] clean]
    `(my-defmethod ~name ~arglist ~@statements)))

(defn ast-dim [_ & ids]
  `(do ~@(map (fn [id] `(def ~id)) (filter symbol? ids)))
  ;; `(my-defvar (quote [~@(filter symbol? ids)]))
  )

(defn ast-boolean [x]
  (if (= x "true") true false))

(defn ast-numerical-entity [n]
  (read-string n))

(defn ast-assignment [varname _ atom]
  ;; `(my-set (quote ~varname) ~atom)
  `(def ~varname ~atom)
  )

(defn slice [xs start end]
  (subvec (vec xs) start end))

(defn fooo [] 3)
(defn bar [] 3)

(defmacro my-fn [args & xs]
  `(fn [~@args] ~@xs))

(defn ast-fun [_ name arglist & statements]
  (let [stmt1 (slice statements 0 (- (count statements) 1))
        stmt2 (slice stmt1 0 (- (count stmt1) 1))
        result (last stmt1)]
    (prn stmt2)
    (prn result)
    `(def ~name (my-fn ~arglist ~@stmt2 ~@result))
    ;; `(def ~name '(fn [] 33))
    ;; `(def ~name (fn [] '~@stmt2 '~@result))
    ))

(defn ast-stub [& xs] xs)

(defn conclusion [] 3)
(defn my-prop [] true)

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
    :boolean 'ast-boolean
    :class_def 'ast-class-def
    :fun 'ast-fun
    :numeric_entity 'ast-numerical-entity
    :ifthen 'ast-if-then
    :prop_acc 'ast-prop-acc
    :mcall 'ast-mcall
    :comp_operator 'ast-comp-operator
    :comparison_expr 'ast-comparison-expr
    :arglist 'ast-arglist
    :args 'ast-args
    :comment 'ast-comment
    :assignment 'ast-assignment
    :dim 'ast-dim
    :atom 'ast-atom}
   tree))

(defn run []
  (->> (get-tree)
       process-tree
       eval
       (recursive-filter is-keepable?)))
