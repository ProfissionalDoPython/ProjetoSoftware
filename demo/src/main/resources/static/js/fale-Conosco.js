function gerarCaptcha() {
  const canvas = document.getElementById("captchaCanvas");
  const ctx = canvas.getContext("2d");

  const n1 = Math.floor(Math.random() * 10) + 5;
  const n2 = Math.floor(Math.random() * 10);
  const operacoes = ["+", "-", "*"];
  const operador = operacoes[Math.floor(Math.random() * operacoes.length)];
  let resultado;

  switch (operador) {
    case "+": resultado = n1 + n2; break;
    case "-": resultado = n1 - n2; break;
    case "*": resultado = n1 * n2; break;
  }

  ctx.fillStyle = "#f2f2f2";
  ctx.fillRect(0, 0, canvas.width, canvas.height);

  for (let i = 0; i < 20; i++) {
    ctx.strokeStyle = `rgba(0,0,0,${Math.random() * 0.2})`;
    ctx.beginPath();
    ctx.moveTo(Math.random() * canvas.width, Math.random() * canvas.height);
    ctx.lineTo(Math.random() * canvas.width, Math.random() * canvas.height);
    ctx.stroke();
  }

  ctx.font = "bold 42px Arial";
  ctx.fillStyle = "#000";
  ctx.fillText(`${n1} ${operador} ${n2}`, 55, 55);

  canvas.dataset.resultado = resultado;
}

gerarCaptcha();

document.getElementById("faleConosco").addEventListener("submit", (e) => {
  e.preventDefault();
  const resposta = document.getElementById("captchaInput").value.trim();
  const correto = document.getElementById("captchaCanvas").dataset.resultado;

  if (resposta !== correto) {
    alert("Captcha incorreto! Tente novamente.");
    gerarCaptcha();
    return;
  }

  alert("Mensagem enviada com sucesso!");
  e.target.reset();
  gerarCaptcha();
});
