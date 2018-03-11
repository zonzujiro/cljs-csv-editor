# cljs-csv-editor

Small learning project on ClojureScript which main purpose is table editor for a two-column CSV format: the first column contains string value (e.g. label), the second contains a numeric value (e.g. quantity).

Input data is loaded as file. Few simple aggregations of the loaded data should be shown alongside the table.

## Setup

To get an interactive development environment run:

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

    lein clean

To create a production build run:

    lein do clean, cljsbuild once min

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL.
