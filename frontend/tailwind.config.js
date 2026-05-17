/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: "class",
  theme: {
    extend: {
      colors: {
        "palid-purple": "#cdb4db",
        "palid-pink": "#ffc8dd",
        "pink": "#ffafcc",
        "palid-blue": "#bde0fe",
        "blue": "#a2d2ff",
        "dark-black": "#2c2c2c",
      },
    },
  },
  plugins: [],
}
