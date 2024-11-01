(ns clojuremain.core)

;; Bind Variables to True/False
(defn bind-values [m exp]
  (if (seq? exp)
    (map #(bind-values m %) exp)  ; Recursively bind in lists
    (get m exp exp)))  ; Handle variables and constants

;; Convert Logical Expressions to NOR Format
(defn nor-convert [exp]
  (cond
    (not (seq? exp)) exp  ; Base case: not a sequence
    (= 'not (first exp))  ; Handle NOT
    (list 'nor (nor-convert (second exp)))
    (= 'and (first exp))  ; Handle AND
    (cons 'nor (map #(list 'nor (nor-convert %)) (rest exp)))
    (= 'or (first exp))   ; Handle OR
    (list 'nor (cons 'nor (map nor-convert (rest exp))))
    :else exp))  ; Default case


;; Simplify NOR Expressions
(defn simplify [exp]
  (cond
    (not (seq? exp)) exp  ; Base case: not a sequence
    (= (first exp) 'nor)  ; Handle NOR expressions
    (let [args (map simplify (rest exp))]
      (cond
        (every? false? args) true  ; All false arguments
        (some true? args) false    ; Any true argument
        :else
        (let [filtered (remove false? args)
              simplified (distinct filtered)]
          (cond
            (= (count simplified) 1)
            (let [arg (first simplified)]
              (if (and (seq? arg) (= (first arg) 'nor))
                (second arg)  ; Simplify nested NOR
                (if (some #(= arg %) (rest simplified))
                  false
                  (list 'nor arg))))
            (some #(= % (list 'nor (first simplified))) (rest simplified))
            (first simplified)
            :else (cons 'nor simplified)))))
    :else (map simplify exp)))  ; Recursively simplify other expressions


;; Evaluate Expressions
(defn evalexp [exp bindings]
  (simplify (nor-convert (bind-values bindings exp))))
