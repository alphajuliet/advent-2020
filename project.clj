(defproject advent "0.1.0-SNAPSHOT"
  :description "Advent of code 2020 exercises."
  :url "http://alphajuliet.com/ns/advent2020#"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [spec-dict "0.2.1"]
                 [ubergraph "0.8.2"]]
  :main ^:skip-aot advent.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
