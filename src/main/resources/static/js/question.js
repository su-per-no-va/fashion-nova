document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('inquiry-form');
  const cancelBtn = document.querySelector('.cancel-btn');

  cancelBtn.addEventListener('click', () => {
    if (confirm('정말로 취소하시겠습니까?')) {
      form.reset();
    }
  });

  form.addEventListener('submit', (event) => {
    event.preventDefault();
    alert('문의가 등록되었습니다.');
    form.reset();
  });
});
