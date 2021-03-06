# this-or-that-game
Learning Clojurescript by making a this-or-that game
```sh
## install node_modules/ folder (one-time setup)
npm install

## start shadow-cljs server
npx shadow-cljs server

# (in a separate terminal window or tab)

## watch the build for development
npx shadow-cljs watch app

## OR

## create a release for production
npx shadow-cljs release app

## (if using watch) connect a browser REPL
npx shadow-cljs cljs-repl app
```