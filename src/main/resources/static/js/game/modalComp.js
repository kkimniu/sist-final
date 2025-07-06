const modal = document.getElementById("orderInfoModal");
const modalHeader = document.getElementById("modalHeader");
let offsetX;
let offsetY;
let isDragging = false;

function showModal(message) {
    modal.style.display = "block";
    document.getElementById("orderInfoModalText").innerText = message;
}
function closeModal() {
    modal.style.display = "none";
}

modalHeader.addEventListener("mousedown", (e) => {
    isDragging = true;
    offsetX = e.clientX - modal.offsetLeft;
    offsetY = e.clientY - modal.offsetTop;
    });

document.addEventListener("mousemove", (e) => {
    if (isDragging) {
        modal.style.left = e.clientX - offsetX + "px";
        modal.style.top = e.clientY - offsetY + "px";
    }
});
document.addEventListener("mouseup", () => {
    isDragging = false;
});