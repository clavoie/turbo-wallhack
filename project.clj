(defproject turbo-wallhack "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "https://github.com/clavoie/turbo-wallhack"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]]
  :global-vars {*warn-on-reflection* true}
  :plugins [[codox "0.8.12"]]
  :codox {:src-dir-uri "http://github.com/clavoie/turbo-wallhack/blob/master/"
          :src-linenum-anchor-prefix "L"})
