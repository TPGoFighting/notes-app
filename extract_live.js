const fs = require("fs");
const vm = require("vm");

const js = fs.readFileSync("live_bundle.js", "utf8");

// Articles
const artStart = js.indexOf("[{number:");
let depth = 0;
let end = artStart;
for (let i = artStart; i < js.length; i++) {
  if (js[i] === '[') depth++;
  else if (js[i] === ']') {
    depth--;
    if (depth === 0) {
      end = i + 1;
      break;
    }
  }
}
const artCode = js.substring(artStart, end);
const articles = vm.runInNewContext(artCode);
console.log("Live extracted articles count:", articles.length);

// Categories
const catStart = js.indexOf("[{slug:`ai-engineering`");
let catEnd = catStart;
depth = 0;
for (let i = catStart; i < js.length; i++) {
  if (js[i] === '[') depth++;
  else if (js[i] === ']') {
    depth--;
    if (depth === 0) {
      catEnd = i + 1;
      break;
    }
  }
}
const catCode = js.substring(catStart, catEnd);
const categories = vm.runInNewContext(catCode);
console.log("Live extracted categories count:", categories.length);
console.log("Live Categories:", categories);

// Glossary
const termStart = js.indexOf("[{term:");
let glossary = [];
if (termStart !== -1) {
  let d = 0;
  let tEnd = termStart;
  for (let i = termStart; i < js.length; i++) {
    if (js[i] === '[') d++;
    else if (js[i] === ']') {
      d--;
      if (d === 0) {
        tEnd = i + 1;
        break;
      }
    }
  }
  const termCode = js.substring(termStart, tEnd);
  glossary = vm.runInNewContext(termCode);
  console.log("Live extracted glossary count:", glossary.length);
}

// Save complete json
fs.writeFileSync("notes_data.json", JSON.stringify({
  updatedAt: new Date().toISOString(),
  categories: categories,
  articles: articles,
  glossary: glossary
}, null, 2));

console.log("Updated notes_data.json with live data!");
