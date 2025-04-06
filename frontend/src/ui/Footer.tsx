export default function Footer() {
  return (
    <footer className="mt-16 mb-4 flex justify-center">
      <a
        href="https://cristianruiz.dev"
        target="_blank"
        className="flex gap-2 "
      >
        cristianruiz.dev 🚀 {new Date().getFullYear()}
      </a>
    </footer>
  );
}
