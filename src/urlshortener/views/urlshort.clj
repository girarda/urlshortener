(ns urlshortener.views.urlshort
  (:require [urlshortener.views.layout :as layout]
            [hiccup.core :refer [h]]
            [hiccup.form :as form]
            [ring.util.anti-forgery :refer :all]
            [hiccup.page :refer :all]
            [hiccup.bootstrap.page :refer :all]))

(defn shorten-form []
  [:div {:id "url-shortener-form"}
   (form/form-to [:post "/"]
                 (form/label "url" "What url do you want to shorten?")
                 (form/text-area "url")
                 (form/submit-button {:class "btn btn-primary"} "SHORTEN!"))])

(defn index []
  (layout/common "URLSHORTENER"
                 (shorten-form)
                 [:div {:class "clear"}]
                 ))

(defn show-short-url [short-url]
  (layout/common "SHORT URL"
                 (hiccup.element/link-to (str "localhost:3000/"short-url) (str "localhost:3000/"short-url))
                 [:div {:class "clear"}]
                 ))