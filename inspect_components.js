const fs = require("fs");
const js = fs.readFileSync("/tmp/notes_repo/assets/index-CMTjVc30.js", "utf8");

function dumpFunction(name) {
  const idx = js.indexOf(`function ${name}(`);
  if (idx !== -1) {
    console.log(`=== FUNCTION ${name} ===`);
    console.log(js.substring(idx, idx + 2000));
  }
}

["Gp", "qp", "Zp", "em", "im", "um", "mm"].forEach(dumpFunction);
