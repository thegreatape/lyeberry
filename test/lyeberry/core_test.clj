(ns lyeberry.core-test
  (:use midje.sweet)
  (:use [lyeberry.core]))

(facts "about minuteman-copies"
  (let [nonexistent-book     {:author "Nobody" :title "Doesn't Exist"}
        multiple-copies-book {:author "Toast"  :title "Charles Stross"}]

    (fact "it returns an empty vector when no copies are present"
          (minuteman-copies nonexistent-book) => [])

    (pending-fact "it return a vector of copies"
      (minuteman-copies multiple-copies-book)
        => [{:title "Supermen : tales of the posthuman future"
             :author "[edited by] Gardner Dozois"
             :status "AVAILABLE"
             :location "Newton"
             :call-number "Short Stories S959D" }
            {:title "Toast"
             :author "Charles Stross"
             :status "Out"
             :location "Cambridge"
             :call-number "SCI FIC Stross, Charles" }
            {:title "Toast"
             :author "Charles Stross"
             :status "AVAILABLE"
             :location "Newton"
             :call-number "Fic"}
            ] )))

(facts "about minuteman-search-url"
  (fact "it returns the start search url for the given book"
    (minuteman-search-url {:title "Toast" :author "Charles Stross"})
      => "https://find.minlib.net/iii/encore/search/C__Stoast%20charles%20stross__Orightresult__U?lang=eng&suite=cobalt"))
