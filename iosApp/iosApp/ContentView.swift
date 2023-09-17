//
//  ContentView.swift
//  nek
//
//  Created by Nekbakht Zabirov on 09.07.2023.
//

import SwiftUI
import shared

struct ViewControllerWrapper: UIViewControllerRepresentable {
    private let composeViewController = ComposeRootControllerKt.getRootController()

    func makeUIViewController(context: UIViewControllerRepresentableContext<ViewControllerWrapper>) -> UIViewController {
        return composeViewController
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: UIViewControllerRepresentableContext<ViewControllerWrapper>) {
        // Update your view controller here
    }
}

struct ContentView: View {
    var body: some View {
        ViewControllerWrapper()
            .frame(minWidth: 0, maxWidth: .infinity, minHeight: 0, maxHeight: .infinity)
            .edgesIgnoringSafeArea(.all)
    }
}
