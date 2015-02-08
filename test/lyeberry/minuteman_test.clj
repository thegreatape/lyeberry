(ns lyeberry.minuteman-test
  (:use midje.sweet)
  (:use [lyeberry.minuteman]))

(def multiple-copies-book {:author "Toast"  :title "Charles Stross"})
(def toast-html (slurp "test/lyeberry/toast-charles-stross.html"))

(facts "about extract-books"
  (let [books (extract-books toast-html)]
    (fact "it return a vector of copies"
      books => (contains {:title "Supermen : tales of the posthuman future"
                          :author "[edited by] Gardner Dozois"
                          :status "AVAILABLE"
                          :location "Newton"
                          :url ""
                          :call_number "Short Stories S959D" })
      books => (contains {:title "Toast"
                          :author "Charles Stross"
                          :status "Out"
                          :location "Cambridge"
                          :url ""
                          :call_number "SCI FIC Stross, Charles" })
      books => (contains {:title "Toast"
                          :author "Charles Stross"
                          :status "AVAILABLE"
                          :location "Newton"
                          :url ""
                          :call_number "Fic"})
         )))

(facts "about search-url"
  (fact "it returns the start search url for the given book"
    (search-url {:title "Toast" :author "Charles Stross"})
      => "https://find.minlib.net/iii/encore/search/C__Stoast%20charles%20stross__Orightresult__U?lang=eng&suite=cobalt"))
