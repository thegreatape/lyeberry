(ns lyeberry.minuteman-test
  (:use midje.sweet)
  (:use [lyeberry.minuteman]))

(def multiple-copies-book {:author "Toast"  :title "Charles Stross"})
(def toast-html (slurp "test/lyeberry/toast-charles-stross.html"))
(def black-prism-html (slurp "test/lyeberry/black-prism-brent-weeks.html"))

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
      ))
  (let [books (extract-books black-prism-html)]
    (fact "it strips interior whitespace from call_number"
      (map :call_number books) => '( "Fic Weeks"
                                     "Fiction/Weeks"
                                     "SCI FIC Weeks, Brent"
                                     "FICTION WEEKS"
                                     "SF"
                                     "FICTION / Weeks"
                                     "FICTION Weeks"
                                     "FIC WEE 1"
                                     "Science Fiction/Weeks. B. SERIES : Lightbringer 1"
                                     "SF Pb"
                                     "SCI FIC Weeks, Brent"
                                     "PB FANTASY Weeks"
                                     "SF Pb"
                                     "SCIENCE FICTION PAPERBACK Weeks, B."))))

(facts "about search-url"
  (fact "it returns the start search url for the given book"
    (search-url {:title "Toast" :author "Charles Stross"})
      => "https://find.minlib.net/iii/encore/search/C__Stoast%20charles%20stross__Orightresult__U?lang=eng&suite=cobalt"))
