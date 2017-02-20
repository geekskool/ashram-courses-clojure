(ns projectv.core
  (:gen-class))

(require '[clojure.java.io :as io])
(require '[clojure.data.json :as json])

(def course (atom {}))
(def file-path "data.json")

(defn addCourse
    [id coursename duration start end]
    (let [thiscourse {"name" coursename,
                      "duration" duration,
                      "start" start,
                      "end" end,
                      "students" {}}]
        (swap! course assoc-in [id] thiscourse)))


(defn addStudent
    [id studentname age gender deposit roomNo]
    (let [student {"age" age,
                   "gender" gender,
                   "deposit" deposit,
                   "roomNo" roomNo,
                   "transactions" []}]
        (swap! course assoc-in [id "students" studentname] student)))


(defn addTransaction
    [id studentName desc quantity rate date]
    (let [transaction {"desc" desc,
                       "quantity" quantity,
                       "rate" rate,
                       "total" (* quantity rate),
                       "date" date}
          length (count (get-in @course [id "students" studentName "transactions"]))]
    (swap! course assoc-in [id "students" studentName "transactions" length] transaction)))


(defn updateDeposit
    [id studentName amount]
    (swap! course update-in [id "students" studentName "deposit"] + amount))


(defn save
    []
    (spit file-path (json/write-str @course)))


(defn main
    []
    (if (.exists (io/as-file file-path))
        (reset! course (json/read-str (slurp file-path)))))


(addCourse "1" "Vipasana" 10 "2017-02-18" "2017-02-28")
(addStudent "1" "Saurabh" 26 "male" 1000 "B5")
(addStudent "1" "Mayank" 24 "male" 1000 "A4")
(addTransaction "1" "Mayank" "Soap" 2 20 "2017-02-18")
(addTransaction "1" "Saurabh" "Water Bottle" 5 15 "2017-02-19")
(updateDeposit "1" "Mayank" 1000)

;(def course (atom {:name "Course 1",
;                   :duration 10
;                   :start "2017-02-18"
;                   :end "2017-02-28"
;                   :students {}}))