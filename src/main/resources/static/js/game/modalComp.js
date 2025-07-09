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

orderPanel.addEventListener("mousedown", (e) => {
    orderPanelIsDragging = true;
    orderOffsetX = e.clientX - orderPanel.offsetLeft;
    orderOffsetY = e.clientY - orderPanel.offsetTop;
});


document.addEventListener("mousemove", (e) => {
    if (isDragging) {
        modal.style.left = e.clientX - offsetX + "px";
        modal.style.top = e.clientY - offsetY + "px";
    }
    if (orderPanelIsDragging) {
        orderPanel.style.left = e.clientX - orderOffsetX + "px";
        orderPanel.style.top = e.clientY - orderOffsetY + "px";
    }
});
document.addEventListener("mouseup", () => {
    isDragging = false;
    orderPanelIsDragging = false;
});