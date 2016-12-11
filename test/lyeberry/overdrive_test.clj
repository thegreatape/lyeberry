(ns lyeberry.overdrive-test
  (:use midje.sweet)
  (:use lyeberry.overdrive))

(def cold-days-html (slurp "test/lyeberry/overdrive-cold-days.html"))
(def extracted-copy {:title "Cold Days"
                     :author "Jim Butcher"
                     :status "Available Copies: 2 of 3 (0 holds)"
                     :url "https://minuteman.overdrive.com/media/1045267" 
                     :location ""
                     :call_number ""})

(facts "about extract-books"
  (let [books (extract-books (media-item-json cold-days-html) minuteman-host)]
    (fact "it return a vector of copies"
      books => (contains extracted-copy))))
