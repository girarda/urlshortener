(ns urlshortener.handler
  (:require [urlshortener.core :refer :all]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defn handle-request [short-url]
  (let [url (retrieve-url short-url)]
    (if (nil? url)
      (route/not-found "Not Found")
      {:status 302
        :headers {"Location" (str "http://" url)}})))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/:short-url" [short-url] (handle-request short-url))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
