const express = require('express');
const dff = require("dialogflow-fulfillment");
const axios = require('axios');
const app = express();
const http = require('http');
const url = "http://localhost:8080/api/eksponati";

var results;

http.get(url, (resp) => {
  let data = '';

  resp.on('data', (chunk) => {
    data += chunk;
  });

  resp.on('end', () => {
    results = JSON.parse(data);
/*     console.log(results.content); */
  });

}).on("error", (err) => {
  console.log("Error: " + err.message);
});


app.get("/", (req, res) => {
  res.send("operativno");
});


app.post("/", express.json(), (req, res) => {
  const agent = new dff.WebhookClient({
    request: req,
    response: res,
  });

async function showAllExibits(agent) {

  let payload = {
      "richContent": [
          []
      ]
  };

  await axios.get("http://localhost:8080/api/eksponati").then(response => {
    var exhibits = response.data.content;

      for (exhibit of exhibits) {
          payload.richContent.push(
            [
              {
                "type": "image",
                "rawUrl": exhibit.photoUrl,
                "accessibilityText": "image",
              },
              {
                "type": "info",
                "naziv": exhibit.naziv,
                "subtitle": "Cena: " + exhibit.cenaObilaska + "€ | Vreme: " + exhibit.vremeObilaska,
                "actionLink": "http://localhost:4200/eksponat/" + exhibit.id,
              }
            ]
          );
      }

      agent.add("U ponudi imamo sledece eksponate.");
      agent.add(new dff.Payload(
          agent.UNSPECIFIED, payload, { sendAsMessage: true, rawPayload: true }
      ));

  });
}

async function findExhibitByTitle(agent) {

    var exhibits;

    await axios.get("http://localhost:8080/api/eksponati").then(response => {
     /*  console.log(response.data); */
      exhibits = response.data.content.filter(ex => ex.naziv.toLowerCase().includes(agent.parameters.keyword.toLowerCase()));
  });

    var outputText = ``;

    for (exhibit of exhibits) {

      outputText += `
      ----------------------------------------------------------
          Id: '${exhibit.id}'\n
          Naziv eksponata: '${exhibit.naziv}'\n
          Opis eksponat: '${exhibit.opis}'\n
          Cena obilaska: ${exhibit.cenaObilaska}\n
          Vreme eksponat: ${exhibit.vremeObilaska }\n
          Zemlja porekla: ${exhibit.zemljaPorekla}\n
      ----------------------------------------------------------\n
      `;
    }

    if (outputText.length === 0) agent.add("Nije pronadjen ni jedan eksponat!");
    else {
        agent.add("Pronadjeni eksponati");
        agent.add(outputText);
    }
}

async function findCheapestExhibit(agent) {



  let payload = {
    "richContent": [
        []
    ]
};

await axios.get("http://localhost:8080/api/eksponati").then(response => {
  var exhibits = response.data.content;

  exhibit = exhibits.reduce((prev, current) => {
    return (prev.cenaObilaska < current.cenaObilaska) ? prev : current
  });

    payload.richContent.push(
      [
        {
          "type": "image",
          "rawUrl": exhibit.photoUrl,
          "accessibilityText": "image",
        },
        {
          "type": "info",
          "naziv": exhibit.naziv,
          "subtitle": "Cena: " + exhibit.cenaObilaska + "€ | Vreme: " + exhibit.vremeObilaska,
          "actionLink": "http://localhost:4200/eksponat/" + exhibit.id,
        }
      ]
    );

    agent.add("Eksponat sa najjeftinijom cenom obilaska je:");
    agent.add(new dff.Payload(
        agent.UNSPECIFIED, payload, { sendAsMessage: true, rawPayload: true }
    ));

});
}

var intentMap = new Map();
intentMap.set('All exibits', showAllExibits);
intentMap.set('Exhibits find by title', findExhibitByTitle);
intentMap.set('Cheapest Exhibit', findCheapestExhibit);

agent.handleRequest(intentMap);

});

app.listen(333, () => console.log("Radi dobro"));
