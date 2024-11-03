(ns partitive.core
  (:require [clojure.string :as str]))

(enable-console-print!)

(println "Program started.")

; client necessities

(defn read-channels []
  "Returns a map of r, g, b"
  (let [r (.-value (.getElementById js/document "red"))
        g (.-value (.getElementById js/document "green"))
        b (.-value (.getElementById js/document "blue"))
        ]
        {:r r
         :g g
         :b b}))

(defn draw-pixel
  "Draw a pixel"
  [ctx color resolution,x y]
  (let [width 800 ;(.-width ctx)
        height 600 ;(.-height ctx)
        dy (/ (float height) resolution)
        dx (/ (float width) resolution 3.0)
        ]
    (do
      (set! (.-fillStyle ctx) (str "rgb(" (:r color) ", 0, 0)"))
      ;(set! (.-fillStyle ctx) (str "rgb(" (:r color) ", 0, " (:r color) ")"))
      (.fillRect ctx x y
                     (+ x dx) dy)
      (set! (.-fillStyle ctx) (str "rgb(0, " (:g color) ", 0)"))
      ;(set! (.-fillStyle ctx) (str "rgb(" (:g color) ", " (:g color) ", 0)"))
      (.fillRect ctx (+ x dx) y
                     (+ x dx) dy)
      (set! (.-fillStyle ctx) (str "rgb(0, 0, " (:b color) ")"))
      ;(set! (.-fillStyle ctx) (str "rgb(0, " (:b color) ", " (:b color) ")"))
      (.fillRect ctx (+ x (* 2 dx)) y
                     (+ x dx) dy)
      )))

(defn refresh-canvas []
  "Update output canvas"
  (let [draw-canvas (.getElementById js/document "draw")
        ctx (.getContext draw-canvas "2d")
        resolution (.-value (.getElementById js/document "resolution"))
        color (read-channels)
        dummy (js/console.log resolution "\n" color)
        drawpx (fn [pt] (draw-pixel ctx color resolution (first pt) (nth pt 1)))
        ]
    (do
      (.clearRect ctx 0 0 (.-width draw-canvas) (.-height draw-canvas))
      (set! (.-fillStyle ctx) (str "rgb(" (:r color) ", " (:g color) ", " (:b color) ")"))
      (doall (map drawpx (for [x (range 0 800 (/ 800.0 resolution))
                               y (range 0 600 (/ 1200.0 resolution))] (list x y))))
      (doall (map drawpx (for [x (range (/ 400.0 resolution) 800 (/ 800.0 resolution))
                               y (range (/ 600.0 resolution) 600 (/ 1200.0 resolution))] (list x y))))
      )))

; event hooks

(set! (.-oninput (.getElementById js/document "resolution")) refresh-canvas)
(set! (.-oninput (.getElementById js/document "red")) refresh-canvas)
(set! (.-oninput (.getElementById js/document "green")) refresh-canvas)
(set! (.-oninput (.getElementById js/document "blue")) refresh-canvas)
