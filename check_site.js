const fs = require("fs");
const https = require("https");
const http = require("http");

async function checkSite() {
  const fetchUrl = (url) => new Promise((resolve, reject) => {
    const client = url.startsWith("https") ? https : http;
    client.get(url, (res) => {
      let data = "";
      res.on("data", chunk => data += chunk);
      res.on("end", () => resolve({ statusCode: res.statusCode, headers: res.headers, body: data }));
    }).on("error", reject);
  });

  try {
    const res = await fetchUrl("https://notes.tpgofighting.top/");
    console.log("Status:", res.statusCode);
    console.log("HTML snippet:", res.body.substring(0, 500));
  } catch (e) {
    console.error("Error fetching notes site:", e);
  }
}

checkSite();
