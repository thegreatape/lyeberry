(ns lyeberry.overdrive-test
  (:use midje.sweet)
  (:use lyeberry.overdrive))

(def martian-html (slurp "test/lyeberry/overdrive-the-martian.html"))
(def root "http://overdrive.bpl.org/D0EE3764-8F41-4B08-9BF8-F46BA471C076/10/50/en/")
(def extracted-copy {:title "The Martian"
                     :author "Andy Weir"
                     :status "Available Copies:  0 of 21"
                     :url "ContentDetails.htm?id=82EF8EC3-386B-4561-8F65-2D59433B9358" })

(facts "about extract-books"
  (let [books (extract-books martian-html)]
    (fact "it return a vector of copies"
      books => (contains extracted-copy))))

(facts "about annotate-copy"
   (fact "it adds an empty location to the hash for compatibility"
         (:location (annotate-copy root extracted-copy)) => "")
   (fact "it adds an empty call_number to the hash for compatibility"
         (:call_number (annotate-copy root extracted-copy)) => "")
   (fact "it changes the url to an abosolute url using the site-root"
         (:url (annotate-copy root extracted-copy)) => (str root "ContentDetails.htm?id=82EF8EC3-386B-4561-8F65-2D59433B9358")))



;; add location call-number and site-root on way out
