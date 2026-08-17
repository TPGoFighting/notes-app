const fs = require("fs");
const vm = require("vm");

const js = fs.readFileSync("/tmp/notes_repo/assets/index-CMTjVc30.js", "utf8");

// Search for glossary array (om or similar)
// Let's find terms like [{term:
const termStart = js.indexOf("[{term:");
console.log("termStart:", termStart);
let glossary = [];
if (termStart !== -1) {
  let depth = 0;
  let end = termStart;
  for (let i = termStart; i < js.length; i++) {
    if (js[i] === '[') depth++;
    else if (js[i] === ']') {
      depth--;
      if (depth === 0) {
        end = i + 1;
        break;
      }
    }
  }
  const termCode = js.substring(termStart, end);
  glossary = vm.runInNewContext(termCode);
  console.log("Glossary count:", glossary.length);
}

// Let's extract full dataset and write it to assets folder for Android
const existing = JSON.parse(fs.readFileSync("notes_data.json", "utf8"));
existing.glossary = glossary;

fs.writeFileSync("notes_data.json", JSON.stringify(existing, null, 2));
console.log("Updated notes_data.json with glossary count:", glossary.length);
