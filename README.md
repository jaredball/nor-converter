# Nor Converter
This Clojure project converts logical expressions consisting of `and`, `or`, and `not` connectives to expressions using only the `nor` connective. After conversion, it symbolically simplifies the resulting `nor` expressions using specific rules.

## Features
- **Logical Expression Conversion:** Transforms logical expressions in prefix notation (Lisp-style lists) using `and`, `or`, and `not` to equivalent forms using only `nor`.
- **Symbolic Simplification:** Simplifies `nor` expressions with arbitrary-length arguments based on predefined simplification patterns.
- **Higher-Order Functions:** Utilizes higher-order functions (e.g., `map`, `filter`) for concise and efficient simplification.

## Usage
1. **Define Expressions:** Create logical expressions as Lisp-style lists, for example:
   ```clojure
   (def p1 '(and x (or x (and y (not z)))))
   (def p2 '(and (and z false) (or x true false)))
   (def p3 '(or true a))
   ```
2. **Run the `evalexp` Function:** The main function `evalexp` takes an expression and a map of variable bindings (e.g., `{x false, z true}`) and returns the simplest form of the expression. For example:
   ```clojure
   (evalexp p1 '{x false, z true})
   ```
3. **Evaluate Simplifications:** `evalexp` recursively simplifies expressions, handling nested and complex cases by:
   - Binding values using `bind-values`.
   - Converting expressions to `nor` form with `nor-convert`.
   - Simplifying the expression with `simplify` based on the rules for `nor`.

## Included Functions
- **`nor-convert`:** Recursively converts `and`, `or`, and `not` expressions to `nor`.
- **`simplify`:** Symbolically simplifies `nor` expressions of arbitrary length.
- **`bind-values`:** Replaces variables in expressions with given values (e.g., `true`, `false`) as specified in a binding map.

## Conversion Rules
These are examples of the conversions `nor-convert` applies:

| Original Expression         | `nor` Form                              |
|-----------------------------|-----------------------------------------|
| `(not x)`                   | `(nor x)`                               |
| `(and x y)`                 | `(nor (nor x) (nor y))`                 |
| `(and x y z)`               | `(nor (nor x) (nor y) (nor z))`         |
| `(and w x y z)`             | `(nor (nor w) (nor x) (nor y) (nor z))` |
| `(or x y)`                  | `(nor (nor x y))`                       |
| `(or x y z)`                | `(nor (nor x y z))`                     |
| `(or w x y z)`              | `(nor (nor w x y z))`                   |

## Simplification Rules
These rules are used to simplify `nor` expressions into their most reduced form:

| Expression                      | Simplified Form                       |
|---------------------------------|---------------------------------------|
| `(nor false)`                   | `true`                                |
| `(nor true)`                    | `false`                               |
| `(nor (nor x))`                 | `x`                                   |
| `(nor (nor (nor x)))`           | `(nor x)`                             |
| `(nor (nor (nor (nor x))))`     | `x`                                   |
| `(nor x x)`                     | `(nor x)`                             |
| `(nor x x x)`                   | `(nor x)`                             |
| `(nor x y)`                     | `(nor x y)`                           |
| `(nor x true)`                  | `false`                               |
| `(nor x false)`                 | `(nor x)`                             |
| `(nor false false)`             | `true`                                |
| `(nor x y false)`               | `(nor x y)`                           |
| `(nor x false false)`           | `(nor x)`                             |
| `(nor false false false)`       | `true`                                |
| `(nor x y true)`                | `false`                               |
| `(nor x y z)`                   | `(nor x y z)`                         |

## Dependencies
- **Clojure language:** Requires Clojure to be installed.

## Notes
- Ensure that expressions are well-formed lists, using only the specified connectives (`and`, `or`, `not`).
- Variable bindings can be partial, leaving some symbols unbound to simplify expressions at a symbolic level.
