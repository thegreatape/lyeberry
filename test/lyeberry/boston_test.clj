(ns lyeberry.boston-test
  (:use midje.sweet)
  (:use [lyeberry.boston]))

(def the-martian-html (slurp "test/lyeberry/bpl-the-martian.html"))
(def the-martian-details-html (slurp "test/lyeberry/bpl-the-martian-details.html"))

(facts "about extract-copies"
   (let [found-copies (extract-copies the-martian-details-html)]
     found-copies => (contains { :title "The Martian By Weir, Andy"
                                 :location "BPS- Boston Latin School"
                                 :call_number "FIC Wei"
                                 :url ""
                                 :status "In" })
     found-copies => (contains { :title "The Martian By Weir, Andy"
                                 :location "BPS- Community Academy of Science and Health"
                                 :call_number "FIC WEIR"
                                 :url ""
                                 :status "In" })
     found-copies => (contains { :title "The Martian By Weir, Andy"
                                 :location "Boston Public Library (2)"
                                 :call_number "SCIFI FANTASY WEIR A"
                                 :url ""
                                 :status "Held" })
     found-copies => (contains { :title "The Martian By Weir, Andy"
                                 :location "Boston Public Library"
                                 :call_number "SCIFI FANTASY WEIR A"
                                 :url ""
                                 :status "Out" })
     found-copies => (contains { :title "The Martian By Weir, Andy"
                                 :location "Malden Public Library"
                                 :call_number "Weir, A."
                                 :url ""
                                 :status "Out" })
     found-copies => (contains { :title "The Martian By Weir, Andy"
                                 :location "BPS- Hyde Park Educational Complex"
                                 :call_number "F WEI"
                                 :url ""
                                 :status "In" })
 ))

(facts "about extract-copy-links"
  (fact "it returns a list of the links to books, without audio or ebooks"
    (extract-copy-links the-martian-html) => ["http://bpl.bibliocommons.com/item/show_circulation/4412580075?search_scope=MBLN" ]))

(facts "about search-url"
   (fact "returns the search url for the given book"
      (search-url {:title "The Martian" :author "Andy Weir"}) =>
      "http://bpl.bibliocommons.com/search?q=the%20martian%20andy%20weir&t=smart&search_category=keyword&commit=Search&submitsearch=go&se=catalog"
      ))

