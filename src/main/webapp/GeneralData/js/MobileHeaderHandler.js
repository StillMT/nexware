document.getElementById('menuToggle').addEventListener('click', function() {
    this.classList.toggle('open');
    document.getElementById('mobileNav').classList.toggle('active');
});