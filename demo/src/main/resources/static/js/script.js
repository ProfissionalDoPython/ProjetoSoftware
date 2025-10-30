const lista = document.querySelector(".lista");
const prev = document.querySelector(".prev");
const next = document.querySelector(".next");

next.addEventListener("click", () => {
  lista.scrollBy({ left: 200, behavior: "smooth" });
});
prev.addEventListener("click", () => {
  lista.scrollBy({ left: -200, behavior: "smooth" });
});

const cards = document.querySelector(".cards");
const prevBtn = document.querySelector(".prev");
const nextBtn = document.querySelector(".next");

nextBtn.addEventListener("click", () => {
  cards.scrollBy({ left: 200, behavior: "smooth" });
});
prevBtn.addEventListener("click", () => {
  cards.scrollBy({ left: -200, behavior: "smooth" });
});

