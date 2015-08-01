(ns urlshortener.handler
  (:require [urlshortener.core :refer :all]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as ring]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [hiccup.form]
            [hiccup.element :only (link-to)]
            [hiccup.page :as page]
            [urlshortener.views.layout :as layout]
            [urlshortener.views.urlshort :as view]
            [ring.middleware.session :refer :all]
            [ring.middleware.anti-forgery :refer :all]
            [clojure.string :as str]))

(defn handle-request [short-url]
  (let [url (retrieve-url short-url)]
    (if (nil? url)
      (route/not-found "Not Found")
      {:status 302
        :headers {"Location" (str "http://" url)}})))

(defn index []
  (view/index))

(defn show-short-url [short-url]
  (view/show-short-url short-url))

(defn create-url [url]
  (when-not (str/blank? url)
    (let [short-url (shorten url)]
  (view/show-short-url short-url))))

(defroutes app-routes
  (GET "/" [] (index))
  (POST "/" [url] (create-url url))
  (GET "/:short-url" [short-url] (handle-request short-url))
  (route/not-found "Not Found"))

(def app (wrap-defaults app-routes (assoc-in site-defaults [:security :anti-forgery] false)))