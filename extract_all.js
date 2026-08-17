const fs = require("fs");
const vm = require("vm");

const js = fs.readFileSync("/tmp/notes_repo/assets/index-CMTjVc30.js", "utf8");

// Let's find categories and articles and glossary and other structures
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
console.log("Extracted articles count:", articles.length);

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
console.log("Extracted categories count:", categories.length);
console.log("Categories:", categories.map(c => c.name));

// Check if there are other datasets like glossary terms
const glossaryMatches = js.match(/\[\{term:`[^`]+`[\s\S]*?\}\]/g) || js.match(/\[\{name:`[^`]+`[\s\S]*?\}\]/g);
console.log("Glossary search matches:", glossaryMatches ? glossaryMatches.length : 0);

// Let's search for any other arrays
const keywords = ["glossary", "terms", "concepts", "timeline", "readingTime", "about"];
for (const kw of keywords) {
  let pos = 0;
  while ((pos = js.indexOf(kw, pos)) !== -1) {
    console.log(`Keyword "${kw}" at index ${pos}`);
    console.log(js.substring(Math.max(0, pos - 50), Math.min(js.length, pos + 200)));
    console.log("---");
    pos += kw.length + 500;
  }
}

// Write the full json
fs.writeFileSync("notes_data.json", JSON.stringify({
  categories: categories,
  articles: articles
}, null, 2));

console.log("Written notes_data.json successfully!");
